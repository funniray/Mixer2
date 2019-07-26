package com.funniray.mixer.interactive.listeners;

import com.funniray.mixer.interactive.ButtonPressEvent;
import com.mixer.interactive.resources.participant.InteractiveParticipant;
import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Listener(references = References.Strong)
public class StringReplacementListener {

    @Handler(priority = 100)
    public void onPress(ButtonPressEvent event) {

        StringBuilder SSLBuilder = new StringBuilder(); //Space Separated List
        StringBuilder CSLBuilder = new StringBuilder(); //Comma Separated List
        StringBuilder HRLBuilder = new StringBuilder(); //Human Readable List

        List<InteractiveParticipant> noDups = new ArrayList<>();

        for (InteractiveParticipant participant : event.getPreviouslyPressed()) {
            if (noDups.contains(participant))
                continue;
            noDups.add(participant);
        }

        for (int i = 0; i < event.getPreviouslyPressed().size() - 1; i++) {
            SSLBuilder.append(event.getPreviouslyPressed().get(i).getUsername()).append(" ");
            CSLBuilder.append(event.getPreviouslyPressed().get(i).getUsername()).append(",");
        }

        if (noDups.size() > 1){
            for (int i = 0; i < noDups.size() - 1; i++) {
                HRLBuilder.append(noDups.get(i).getUsername()).append(" ");
            }
            HRLBuilder.append("and ").append(noDups.get(noDups.size() - 1).getUsername());
        }else {
            HRLBuilder.append(event.getUserPressed().getUsername());
        }

        SSLBuilder.append(event.getPreviouslyPressed().get(event.getPreviouslyPressed().size()-1).getUsername());
        CSLBuilder.append(event.getPreviouslyPressed().get(event.getPreviouslyPressed().size()-1).getUsername());

        ConcurrentHashMap<String,String> replacement = event.getTextReplacement();

        replacement.put("SSL",SSLBuilder.toString());
        replacement.put("CSL",CSLBuilder.toString());
        replacement.put("HRL",HRLBuilder.toString());
        replacement.put("presser",event.getUserPressed().getUsername());
        replacement.put("streamer",event.getInteractive().getMixer().getPlayer().getName());
        replacement.put("input",event.getTextInput());
    }

}
