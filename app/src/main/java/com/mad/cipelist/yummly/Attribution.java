package com.mad.cipelist.yummly;

/**
 * Created by Felix on 6/10/16.
 */
public class Attribution
{
    private String logo;

    private String text;

    private String html;

    private String url;

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
