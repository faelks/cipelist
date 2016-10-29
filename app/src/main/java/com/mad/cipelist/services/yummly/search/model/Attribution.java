package com.mad.cipelist.services.yummly.search.model;

/**
 * Created by Felix on 6/10/16.
 */
public class Attribution
{
    private String logo;

    private String text;

    private String html;

    private String url;

    public Attribution(String logo, String text, String html, String url) {
        this.logo = logo;
        this.text = text;
        this.html = html;
        this.url = url;
    }

    public String getLogo ()
    {
        return logo;
    }

    public void setLogo (String logo)
    {
        this.logo = logo;
    }

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getHtml ()
    {
        return html;
    }

    public void setHtml (String html)
    {
        this.html = html;
    }

    public String getUrl ()
    {
        return url;
    }

    public void setUrl (String url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [logo = "+logo+", text = "+text+", html = "+html+", url = "+url+"]";
    }
}
