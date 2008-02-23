<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:atom="http://www.w3.org/2005/Atom"
	xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">

	<xsl:template match="/">
		<html>
			<head>
				<title><xsl:value-of select="feed/title" /></title>
				<style type="text/css">
					body {
						font-family: sans-serif;
					}
					
					#header {
						margin-bottom: 0.5em;
					}
					
					#header #logo {
						display: inline;
						vertical-align: middle;
						margin-right: 1em;
					}
					
					#header #form {
						display: inline;
						vertical-align: middle;
					}
					
					#summary {
						background-color: #d5ddf3;
						border-top: 1px solid #3366cc;
						text-align: right;
						padding: 0.5em;
						margin-bottom: 1em;
					}
					
					h2 {
						font: inherit;
						font-size: 125%;
						padding: 0px;
						margin: 0px;
					}
					
					.result {
						margin-top: 0.5em;
						margin-bottom: 0.5em;
					}
					
					.link {
						color: #008000;
					}
				</style>
			</head>
			
			<body>
			
			<!-- <h1><xsl:value-of select="feed/title" /></h1> -->
			
			<div id="header">
				<img id="logo">
					<xsl:attribute name="src">
						<xsl:value-of select="feed/logo" />
					</xsl:attribute>
				</img>
				
				<form method="get" id="form">
					<input type="text" name="query">
						<xsl:attribute name="value">
							<xsl:value-of select="feed/Query[@role='request']/@searchTerms" />
						</xsl:attribute>
					</input>
					<input type="submit" value="Search" />
				</form>
			</div>
			
			<!-- <div>Displaying results <xsl:value-of select="feed/opensearch:startIndex" /> to ? of <xsl:value-of select="feed/opensearch:totalResults" />.</div> -->
			<div id="summary">Results <strong><xsl:value-of select="feed/opensearch:startIndex" /> - ?</strong> of <strong><xsl:value-of select="feed/opensearch:totalResults" /></strong> for <strong><xsl:value-of select="feed/Query[@role='request']/@searchTerms" /></strong>.</div>
			
			<xsl:for-each select="feed/entry">
			<div class="result">
				<h2>
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="link/@href" />
						</xsl:attribute>
						<xsl:value-of select="title" />
					</a>
				</h2>
				<xsl:value-of select="content" />
				<div class="link"><xsl:value-of select="link/@href" /></div>
			</div>
			</xsl:for-each>
			
			<a>
				<xsl:attribute name="href">
					<xsl:value-of select="feed/link[@rel=next]/@href" />
				</xsl:attribute>
				Next page
			</a>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>