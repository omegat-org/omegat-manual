<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>


  <!-- Index / ToC -->
  <xsl:param name="generate.index">1</xsl:param>
  <xsl:param name="toc.section.depth">1</xsl:param>
  <xsl:param name="toc.max.depth">2</xsl:param>
  <xsl:param name="generate.section.toc.level">1</xsl:param>


  <!-- Names, titles, captions... -->
  <xsl:param name="use.id.as.filename">1</xsl:param>
  <xsl:param name="chapter.autolabel" select="1"/>


  <!-- Admonitions -->
  <xsl:param name="admon.graphics">0</xsl:param>
  <xsl:param name="admon.style"></xsl:param>
    <!-- default margins overridden as this can't be done in
         the CSS; if we want margins we'll put them in the CSS -->


  <!-- Navigation -->

  <!-- Color of link bar at top and bottom of each web page: -->
  <xsl:variable name="linkbar-bgcolor" select="'#F0F0F0'"/>

  <!-- True: up arrow will link to Firebird Documentation Index if the top of the current
       document is reached.
       False: up arrow disabled at top of doc; need to use explicit doc index links. -->
  <xsl:param name="nav-up-to-docindex" select="false()"/>


  <!-- misc params -->
  <xsl:param name="segmentedlist.as.table" select="1"/>
  <xsl:param name="spacing.paras">1</xsl:param>
  <xsl:param name="html.stylesheet">omegat.css</xsl:param>
  <!-- Override the callout images location -->
  <xsl:param name="callout.graphics.path" select="'images/callouts/'"/>


</xsl:stylesheet>
