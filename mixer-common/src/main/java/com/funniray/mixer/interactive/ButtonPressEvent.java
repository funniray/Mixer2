package com.funniray.mixer.interactive;

import com.funniray.mixer.Mixer;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.control.InteractiveControl;
import com.mixer.interactive.resources.participant.InteractiveParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ButtonPressEvent extends InteractiveEvent {

    private InteractiveParticipant userPressed;
    private InteractiveControl interactiveControl;
    private List<InteractiveParticipant> previouslyPressed;
    private ConcurrentHashMap<String, String> textReplacement;
    private Mixer mixer;
    private Interactive interactive;
    private String textInput;

    public ButtonPressEvent(Interactive interactive, Mixer mixer, InteractiveControl interactiveControl){
        this.mixer = mixer;
        this.interactive = interactive;
        this.interactiveControl = interactiveControl;
        this.reset();
    }

    void reset(){
        this.previouslyPressed = new ArrayList<>();
        this.textReplacement = new ConcurrentHashMap<>();
        if (this.interactiveControl instanceof ButtonControl) {
            ButtonControl control = (ButtonControl) this.interactiveControl;
            control.setProgress(0F);
            control.setCooldown(0);
        }
    }

    ButtonPressEvent press(InteractiveParticipant user, String textInput) {
        this.userPressed = user;
        this.textInput = textInput;
        this.previouslyPressed.add(user);
        this.mixer.getEventBus().post(this).now();
        return this;
    }

    public String replaceString(String in) {
        String out = in;
        for(Map.Entry<String, String> stringEntry : textReplacement.entrySet()) {
            out = out.replace("%"+stringEntry.getKey()+"%", stringEntry.getValue());
        }
        return out;
    }

    public ConcurrentHashMap<String,String> getTextReplacement() {
        return this.textReplacement;
    }

    public ConcurrentHashMap<String, String> getGlobalVars() {
        return this.interactive.globalVars;
    }

    public ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> getLocalVars() {
        return this.interactive.localVars;
    }

    public void setVariableForPresser(InteractiveParticipant user, String variableName, String variableText) {
        if (this.getLocalVars().containsKey(user.getUserID())) {
            this.getLocalVars().get(user.getUserID()).put(variableName,variableText);
        } else {
            ConcurrentHashMap<String,String> vars = new ConcurrentHashMap<>();
            vars.put(variableName,variableText);
            this.getLocalVars().put(user.getUserID(),vars);
        }
    }

    public ConcurrentHashMap<String,String> getLocalVarsForPresser(InteractiveParticipant user) {
        return this.getLocalVars().get(user.getUserID());
    }

    public String getTextInput() {
        return textInput;
    }

    public InteractiveParticipant getUserPressed() {
        return userPressed;
    }

    public InteractiveControl getInteractiveControl() {
        return interactiveControl;
    }

    public List<InteractiveParticipant> getPreviouslyPressed() {
        return this.previouslyPressed;
    }

    public Interactive getInteractive() {
        return interactive;
    }
}
