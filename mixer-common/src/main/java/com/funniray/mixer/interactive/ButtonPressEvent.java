package com.funniray.mixer.interactive;

import com.funniray.mixer.Mixer;
import com.mixer.interactive.resources.control.ButtonControl;
import com.mixer.interactive.resources.participant.InteractiveParticipant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ButtonPressEvent extends InteractiveEvent {

    private InteractiveParticipant userPressed;
    private ButtonControl interactiveControl;
    private List<InteractiveParticipant> previouslyPressed;
    private ConcurrentHashMap<String, String> textReplacement;
    private Mixer mixer;

    public ButtonPressEvent(Mixer mixer, ButtonControl interactiveControl){
        this.mixer = mixer;
        this.interactiveControl = interactiveControl;
        this.reset();
    }

    void reset(){
        this.previouslyPressed = new ArrayList<>();
        this.textReplacement = new ConcurrentHashMap<>();
        this.interactiveControl.setProgress(0F);
        this.interactiveControl.setCooldown(0);
    }

    ButtonPressEvent press(InteractiveParticipant user) {
        this.userPressed = user;
        this.previouslyPressed.add(user);
        this.mixer.getEventBus().post(this);
        return this;
    }

    public String replaceString(String in) {
        String out = in;
        for(Map.Entry<String, String> stringEntry : textReplacement.entrySet()) {
            out = out.replace(stringEntry.getKey(), stringEntry.getValue());
        }
        return out;
    }

    public ConcurrentHashMap<String,String> getTextReplacement() {
        return this.textReplacement;
    }


    public InteractiveParticipant getUserPressed() {
        return userPressed;
    }

    public ButtonControl getInteractiveControl() {
        return interactiveControl;
    }

    public List<InteractiveParticipant> getPreviouslyPressed() {
        return this.previouslyPressed;
    }

}
