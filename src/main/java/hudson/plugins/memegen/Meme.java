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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 *
 * @author Jon Cairns <jon.cairns@22blue.co.uk>
 */
public class Meme implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7408263024860013752L;
	
	protected int generatorID;
    protected int imageID;
    protected boolean parsed = false;
    public String identifier;
    public String upperText;
    public String lowerText;
    public String additionalAttributes;

    protected String imageURL;

    @DataBoundConstructor
    public Meme(String identifier, String lowerText, String upperText, String additionalAttributes) {
        this.identifier = identifier;
        this.upperText = upperText;
        this.lowerText = lowerText;
        this.additionalAttributes = additionalAttributes;
    }

    public String getUpperText() {
        return upperText;
    }

    public String getLowerText() {
        return lowerText;
    }

    public String getImageURL() {
        return "http://apimeme.com/meme?meme=" + identifier + "&top=" + encode(upperText) + "&bottom=" + encode(lowerText);
    }
    
    public String getAdditionalAttributes() {
		return (additionalAttributes != null ? additionalAttributes : "");
	}

    public Meme clone() {
        return new Meme(identifier, lowerText, upperText, additionalAttributes);
    }

    private static String encode(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

}
