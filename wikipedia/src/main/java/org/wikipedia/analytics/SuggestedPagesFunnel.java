package org.wikipedia.analytics;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.wikipedia.PageTitle;
import org.wikipedia.Site;
import org.wikipedia.WikipediaApp;

import java.util.List;

public class SuggestedPagesFunnel extends Funnel {
    private static final String SCHEMA_NAME = "MobileWikiAppArticleSuggestions";
    private static final int REV_ID = 10590869;

    private final String appInstallID;
    private final Site site;

    public SuggestedPagesFunnel(WikipediaApp app, Site site) {
        super(app, SCHEMA_NAME, REV_ID);

        //Retrieve this app installation's unique ID, used to record unique users of features
        appInstallID = app.getAppInstallID();

        this.site = site;
    }

    @Override
    protected JSONObject preprocessData(JSONObject eventData) {
        try {
            eventData.put("appInstallID", appInstallID);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return eventData;
    }

    protected void log(Object... params) {
        super.log(site, params);
    }

    public void logSuggestionsShown(PageTitle currentPageTitle, List<PageTitle> suggestedTitles) {
        log(
                "action", "shown",
                "pageTitle", currentPageTitle.getDisplayText(),
                "readMoreList", TextUtils.join("|", suggestedTitles)
        );
    }

    public void logSuggestionClicked(PageTitle currentPageTitle, List<PageTitle> suggestedTitles,
                                     int clickedIndex) {
        log(
                "action", "clicked",
                "pageTitle", currentPageTitle.getDisplayText(),
                "readMoreList", TextUtils.join("|", suggestedTitles),
                "readMoreIndex", clickedIndex
        );
    }

}
