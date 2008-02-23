<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:atom="http://www.w3.org/2005/Atom">

	<xsl:template match="/">
		<html>
			<head>
				<title>Service</title>
			</head>
			<body>
				<xsl:for-each select="service/workspace">
					<h2>
						<xsl:value-of select="atom:title" />
					</h2>
					
					<ul>
					<xsl:for-each select="collection">
						<li>
							<a>
							<xsl:attribute name="href">
								<xsl:value-of select="@href" />
							</xsl:attribute>
							<xsl:value-of select="atom:title" />
							</a>
						</li>
					</xsl:for-each>
					</ul>
				</xsl:for-each>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>