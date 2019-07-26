package com.funniray.mixer.interactive;

import com.funniray.mixer.Mixer;
import com.funniray.mixer.config.Config;
import com.funniray.mixer.iPlayer;
import com.funniray.mixer.interactive.listeners.*;
import com.google.common.eventbus.Subscribe;
import com.mixer.interactive.GameClient;
import com.mixer.interactive.event.connection.ConnectionEstablishedEvent;
import com.mixer.interactive.event.control.input.*;
import com.mixer.interactive.event.participant.ParticipantJoinEvent;
import com.mixer.interactive.event.participant.ParticipantLeaveEvent;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.control.InteractiveControlType;
import com.mixer.interactive.resources.control.TextboxControl;
import com.mixer.interactive.resources.group.InteractiveGroup;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import com.mixer.interactive.resources.scene.InteractiveScene;
import com.mixer.interactive.services.SceneServiceProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static com.mixer.interactive.GameClient.GROUP_SERVICE_PROVIDER;

public class Interactive {

    private Config config;
    private iPlayer player;
    private GameClient client;
    private Mixer mixer;

    private ConcurrentHashMap<String, InteractiveParticipant> participantHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveControl> controlHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveGroup> groupHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, InteractiveScene> sceneHashMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ButtonPressEvent> buttonHashMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, String> globalVars = new ConcurrentHashMap<>();
    ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> localVars = new ConcurrentHashMap<>();

    public Interactive(Config config, String token, iPlayer player, Mixer mixer) {
        this.config = config;
        this.player = player;
        this.mixer = mixer;

        client = new GameClient(this.config.projectID, "d04e85fd1cb06e4eb9891fc118fe75893eca399955189926");
        client.connect(token,config.shareCode);
        client.getEventBus().register(this);

        this.mixer.getEventBus().subscribe(new TimeoutListener());
        this.mixer.getEventBus().subscribe(new SwitchWindowListener());
        this.mixer.getEventBus().subscribe(new StringReplacementListener());
        this.mixer.getEventBus().subscribe(new SetVariableListener());
        this.mixer.getEventBus().subscribe(new RequiredClicksListener());
    }

    @Subscribe
    public void onParticipantJoin(ParticipantJoinEvent event) {
        Set<InteractiveParticipant> participants = event.getParticipants();
        for (InteractiveParticipant participant : participants) {
            //player.sendMessage("&9[Mixer] >>> " + participant.getUsername() + "["+participant.getSessionID()+"] joined with group " + participant.getGroupID());
            participantHashMap.putIfAbsent(participant.getSessionID(), participant);
        }
    }

    @Subscribe
    public void onParticipantLeave(ParticipantLeaveEvent event){
        for (InteractiveParticipant user : event.getParticipants()) {
            participantHashMap.remove(user.getSessionID());
            //player.sendMessage("&9&l[Mixer]&r&9 >>> " + user.getUsername() + " left");
        }
    }

    @Subscribe
    public void onConnectionEsablished(ConnectionEstablishedEvent event){
        try {
            if (!client.isConnected()) {
                player.sendMessage("&9&l[Mixer]&r&4 Connection was established but isn't connected!");
            }else{
                player.sendMessage("&9&l[Mixer]&r&9 Interactive connected!");
            }

            Set<InteractiveScene> scenes = new SceneServiceProvider(client).getScenes().get();
            for(InteractiveScene scene:scenes){
                Set<InteractiveControl> controls = scene.getControls();
                for(InteractiveControl control:controls){
                    //player.sendMessage("&9&l[Mixer]&r&9 >>> "+control.getControlID()+" is "+control.getKind()+" on "+control.getSceneID());
                    controlHashMap.put(control.getControlID(),control);
                    if (control.getKind() == InteractiveControlType.BUTTON || control.getKind() == InteractiveControlType.TEXTBOX) {
                        buttonHashMap.put(control.getControlID(), new ButtonPressEvent(this, mixer, control));
                    }
                }

                sceneHashMap.put(scene.getSceneID(),scene);
                if (!scene.getSceneID().equals("default")) {
                    InteractiveGroup group = new InteractiveGroup(scene.getSceneID());
                    group.setScene(scene);
                    groupHashMap.put(scene.getSceneID(), group);
                }
            }
            client.using(GROUP_SERVICE_PROVIDER).create(groupHashMap.values()).get();
            InteractiveGroup defaultGroup = new InteractiveGroup("default");
            defaultGroup.setScene("default");
            groupHashMap.put("default",defaultGroup);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        client.ready(true);
    }

    @Subscribe
    public void onKeyDown(ControlInputEvent event){
        if (event instanceof ControlKeyUpEvent || event instanceof ControlMouseUpInputEvent) {
            return;
        }
        InteractiveParticipant participant = participantHashMap.get(event.getParticipantID());
        if (participant == null)
            return;
        //player.sendMessage("&9&l[Mixer]&r&9 >>> " + participant.getUsername() + " pressed " + event.getControlInput().getControlID());
        //player.sendMessage("&9&l[Mixer]&r&9 >>> Control: " + event.getControlInput().getRawInput());
        ButtonPressEvent val = buttonHashMap.get(event.getControlInput().getControlID());
        String submitText = null;
        if (val.getInteractiveControl() instanceof TextboxControl) {
            submitText = event.getControlInput().getRawInput().get("value").getAsString();
        }
        val.press(participant, submitText);
        if (val.shouldCharge()&&event.getTransaction()!=null) {
            new Thread(() -> {
                try {
                    event.getTransaction().capture(this.client).get();
                } catch (InterruptedException | ExecutionException e) {
                    //e.printStackTrace(); //There's no reply, don't care about this
                }
            }).start();
        }
        if (val.shouldReset()) {
            val.reset();
        }
        if (val.shouldUpdate()){
            this.updateControl(val.getInteractiveControl());
        }
    }

    public void updateControl(InteractiveControl control){
        new Thread(() -> {
            try {
                client.using(GameClient.CONTROL_SERVICE_PROVIDER).update(control).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        }).start();
    }

    public void updateControls(Collection<InteractiveControl> controls){
        new Thread(() -> {
            try {
                client.using(GameClient.CONTROL_SERVICE_PROVIDER).update(controls).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        }).start();
    }

    public void switchSceneForParticipant(InteractiveParticipant participant, String group){
        participant.changeGroup(getGroup(group));
        new Thread(() -> {
            try {
                client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(participant).get();
            } catch (InterruptedException | ExecutionException e) {
                //I don't really care what happens here
            }
        }).start();
    }

    private InteractiveGroup getGroup(String groupString){
        if(groupHashMap.containsKey(groupString)){
            return groupHashMap.get(groupString);
        }
        return null;
    }

    public void disconnect(){
        this.client.disconnect();
        player.sendMessage("&9&l[Mixer]&r&9 Interactive disconnected");
    }

    public void resetScene(String sceneID){
        List<InteractiveControl> updated = new ArrayList<>();
        for (ButtonPressEvent button:this.buttonHashMap.values()){
            if (button.getInteractiveControl().getSceneID().equals(sceneID)){
                button.reset();
                updated.add(button.getInteractiveControl());
            }
        }
        if (!updated.isEmpty()){
            this.updateControls(updated);
        }
    }

    public void switchAllScenes(String scene){
        InteractiveGroup group = this.getGroup("default").setScene(scene);
        for(InteractiveParticipant participant:this.participantHashMap.values()){
            participant.changeGroup("default");
        }

        new Thread(()->{
            client.using(GameClient.PARTICIPANT_SERVICE_PROVIDER).update(this.participantHashMap.values());
            client.using(GROUP_SERVICE_PROVIDER).update(group);
        }).start();
    }

    public Mixer getMixer() {
        return this.mixer;
    }
}


//new Thread(runnable).start();