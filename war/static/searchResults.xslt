<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:atom="http://www.w3.org/2005/Atom"
	xmlns:opensearch="http://a9.com/-/spec/opensearch/1.1/">

	<xsl:template match="/">
		<html>
			<head>
				<title><xsl:value-of select="feed/title" /></title>
				<link rel="icon" href="http://www.lucene-ws.net/images/magnifying_glass.png" />
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
					
					#header #form .text {
						width: 33%;
					}
					
					#summary {
						background-color: #d5ddf3;
						border-top: 1px solid #3366cc;
						text-align: right;
						padding: 0.5em;
						margin-bottom: 1em;
						min-height: 1em;
					}
					
					.suggestion {
						color: #cc0000;
						margin-bottom: 1em;
					}
					
					.suggestion a {
						font-style: italic;
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
			
			<xsl:variable name="startIndex">
				<xsl:value-of select="feed/opensearch:startIndex" />
			</xsl:variable>
			
			<xsl:variable name="resultCount">
				<xsl:value-of select="count(feed/entry)" />
			</xsl:variable>
			
			<xsl:variable name="endIndex">
				<xsl:value-of select="$startIndex + $resultCount - 1" />
			</xsl:variable>
			
			<xsl:variable name="totalResults">
				<xsl:value-of select="feed/opensearch:totalResults" />
			</xsl:variable>
			
			<xsl:variable name="searchTerms">
				<xsl:value-of select="feed/Query[@role='request']/@searchTerms" />
			</xsl:variable>
			
			<div id="header">
				<img id="logo">
					<xsl:attribute name="src">
						<xsl:value-of select="feed/logo" />
					</xsl:attribute>
				</img>
				
				<form method="get" id="form">
					<input type="text" name="query" class="text">
						<xsl:attribute name="value">
							<xsl:value-of select="$searchTerms" />
						</xsl:attribute>
					</input>
					<input type="submit" value="Search" />
				</form>
			</div>
			
			<!-- <div>Displaying results <xsl:value-of select="feed/opensearch:startIndex" /> to ? of <xsl:value-of select="feed/opensearch:totalResults" />.</div> -->
			<div id="summary">
				<xsl:if test="$resultCount &gt; 0">
					<span>Results <strong><xsl:value-of select="$startIndex" /> - <xsl:value-of select="$endIndex" /></strong> of <strong><xsl:value-of select="$totalResults" /></strong> for <strong><xsl:value-of select="feed/Query[@role='request']/@searchTerms" /></strong>.</span>
				</xsl:if>
			</div>
			
			<xsl:for-each select="feed/Query[@role='correction']">
				<div class="suggestion">
					Did you mean: <a><xsl:attribute name="href"><xsl:value-of select="@searchTerms" /></xsl:attribute><xsl:value-of select="@searchTerms" /></a>
				</div>
			</xsl:for-each>
			
			<xsl:choose>
				<xsl:when test="$totalResults &gt; 0">
					<xsl:for-each select="feed/entry">
					<xsl:variable name="relevance" select=".75" />
					<div class="result">
						<h2>
							<a>
								<xsl:attribute name="href">
									<xsl:value-of select="link/@href" />
								</xsl:attribute>
								<xsl:value-of select="title" />
							</a>
						</h2>
						<div class="relevance" style="width: 20%; border: 1px solid Black;"><span style="width: ${$relevance * 100}%; background-color: Red;">boo</span></div>
						<xsl:value-of select="content/div" />
						<div class="link"><xsl:value-of select="link/@href" /></div>
					</div>
					</xsl:for-each>
			
					<a>
						<xsl:attribute name="href">
							<xsl:value-of select="feed/link[@rel=next]/@href" />
						</xsl:attribute>
						Next page
					</a>
				</xsl:when>
				<xsl:otherwise>
					<div>Your search - <strong><xsl:value-of select="$searchTerms" /></strong> - did not match any documents.</div>
				</xsl:otherwise>
			</xsl:choose>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>