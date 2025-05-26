<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://www.w3.org/1999/xhtml"
                xmlns:whc="http://www.xmlmind.com/whc/schema/whc"
                xmlns:htm="http://www.w3.org/1999/xhtml"
                version="1.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <whc:toc>
            <!-- Apply templates only to the top-level 'ul' elements in the 'list-of-titles' -->
            <xsl:apply-templates select="//htm:div[@class='list-of-titles']//htm:ul[@class='toc']"/>
        </whc:toc>
    </xsl:template>

    <xsl:template name="toc.item">
        <whc:entry href="{htm:a/@href}">
            <whc:title>
                <!-- Extract and combine title text -->
                <xsl:apply-templates select="htm:a//text()"/>
            </whc:title>
            <!-- Only process direct child <li> elements nested under the current <ul> -->
            <xsl:apply-templates select="htm:ul[@class='toc']/htm:li"/>
        </whc:entry>
    </xsl:template>

    <xsl:template match="htm:ul[@class='toc']/htm:li">
        <xsl:call-template name="toc.item"/>
    </xsl:template>

</xsl:stylesheet>