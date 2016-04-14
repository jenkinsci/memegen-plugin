/*
 * The MIT License
 *
 * Copyright 2012 Jon Cairns <jon.cairns@22blue.co.uk>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package hudson.plugins.memegen;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.User;
import java.util.ArrayList;
import java.util.Set;
import java.util.Iterator;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

class NoMemesException extends Exception {

    NoMemesException() {
        super("There are no Memes for this context: please create some in the Jenkins configuration");
    }
}

/**
 *
 * @author Jon Cairns <jon.cairns@22blue.co.uk>
 */
public class MemeFactory {

    public static Meme getMeme(ArrayList<Meme> memes, AbstractBuild build) throws NoMemesException {
        String resultString = build.getResult().toString();
        Meme meme = selectMeme(memes, build.getResult());
        String buildName = build.getDisplayName();
        String projectName = build.getProject().getDisplayName();
        String users = userSetToString(build.getCulprits());
        meme.lowerText = textReplace(meme.lowerText, buildName, projectName, users);
        meme.upperText = textReplace(meme.upperText, buildName, projectName, users);
        return meme;
    }

    protected static Meme selectMeme(ArrayList<Meme> memes, Result type) throws NoMemesException {
        int size = memes.size();
        switch (size) {
            case 0:
                throw new NoMemesException();
            case 1:
                return memes.get(0).clone();
            default:
                Random rand = new Random();
                int key = rand.nextInt(memes.size());
                return memes.get(key).clone();
        }
    }

    protected static String textReplace(String input, String buildNumber, String projectName) {
        String text = input;
        if (text.matches(".*\\$\\{day\\}.*")) {
            Calendar now = Calendar.getInstance();

            //create an array of days
            String[] strDays = new String[]{
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thusday",
                "Friday",
                "Saturday"
            };

            String day = strDays[now.get(Calendar.DAY_OF_WEEK) - 1];
            text = text.replace("${day}", day);
        }
        text = text.replace("${build}", buildNumber);
        text = text.replace("${project}", projectName);
        return text;
    }

    protected static String textReplace(String input, String buildNumber, String projectName, String user) {
        String text = textReplace(input, buildNumber, projectName);
        return text.replace("${user}", user);
    }

    protected static String userSetToString(Set userSet) {
        Iterator i = userSet.iterator();
        String ret = "";
        int idx = 0;
        while (i.hasNext()) {
            User user = (User) i.next();
            if (idx > 0) {
                ret += ", ";
            }
            ret += user.getDisplayName();
            idx++;
        }
        // Make sure last comma becomes an " and " if it exists
        var ind = ret.lastIndexOf(",");
        if(ind != -1) {
            ret = ret.substring(0,ind) + " and" + ret.substring(ind + 1);
        }
        if (ret.isEmpty()) {
            ret = "Nobody";
        }
        return ret;
    }
}
