package com.example.android.petcbt;

import java.util.HashMap;

/**
 * Created by ronniehung on 2017-09-16.
 */

public class Cat {

    HashMap<String, String> emotionKeyWordToSuggestion = new HashMap<String, String>();

    String makeWittyRemark() {
        // makes a witty remark
        return "Meow";
    }
    String giveReinforcement() {
        // makes a positive reinforcing remark when it's happy
        return "Life is great!";
    }

    static String getResponseFromFeeling(String feeling) {
        // e.g. feeling <- "I feel miserable"
        // lookup the appropriate response for "I feel responsibe"
        // return response;
        return feeling;
    }

    // getSuggestionFromSuggestionType(String suggestionType) {
    //    let suggestions <- hashmap that maps from suggestionType to a suggestion String
    //    String suggestion = suggestions[suggestionType];
    //    return suggestion;
    // }

    String getSuggestionFromEmotionKeyWord(String emotionKeyWord) {
        // returns value paired with given emotionKeyWord in hashmap that maps from suggestionType to a suggestion String
//        return this.emotionKeyWordToSuggestion.get(suggestionType);
        return "Foo";
    }
}
