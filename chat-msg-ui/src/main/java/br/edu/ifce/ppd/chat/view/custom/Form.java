package br.edu.ifce.ppd.chat.view.custom;

import java.util.List;

/**
 * Created by andrecoelho on 5/21/16.
 */
public interface Form {

    boolean isFormValid();

    List<String> getLastInvalidStatus();

}
