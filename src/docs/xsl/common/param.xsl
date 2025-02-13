<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>


  <!-- OVERRIDDEN STYLESHEET PARAMETERS: -->

  <!-- These two have been moved here because they should be the same
       for each build target (HTML, PDF etc.) -->
  <xsl:param name="runinhead.default.title.end.punct" select="':'"/>
  <xsl:param name="runinhead.title.end.punct" select="'.!?:-'"/>

  <!-- PARAMETERS INTRODUCED BY US -->

  <xsl:param name="omegat-home.url"   select="'http://www.omegat.org'"/>
  <xsl:param name="omegat-home.title" select="'OmegaT Home'"/>

  <xsl:param name="omegat-docindex.url"   select="'http://omegat.sourceforge.io/documentation/standard/'"/>
  <xsl:param name="omegat-docindex.title" select="'OmegaT Documentation Index'"/>

  <xsl:param name="runinhead.bold"   select="1"/>
  <xsl:param name="runinhead.italic" select="0"/>

</xsl:stylesheet>
