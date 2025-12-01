<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Órarend – XBG3S6</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 20px; }
                    h2 { color: #6e2b8b; }
                    table { border-collapse: collapse; }
                    th, td { border: 1px solid #6e2b8b; padding: 4px 8px; }
                    th { background-color: #caa3d8; }
                    td { background-color: #e6c9f0; }
                </style>
            </head>
            <body>

                <h2>Órarend – for-each, value-of</h2>

                <table>
                    <tr>
                        <th>ID</th>
                        <th>Típus</th>
                        <th>Tárgy</th>
                        <th>Nap</th>
                        <th>Időpont</th>
                        <th>Helyszín</th>
                        <th>Oktató</th>
                        <th>Szak</th>
                    </tr>

                    <!-- minden óra bejárása -->
                    <xsl:for-each select="XBG3S6_orarend/ora">
                        <tr>
                            <td><xsl:value-of select="@id"/></td>
                            <td><xsl:value-of select="@tipus"/></td>
                            <td><xsl:value-of select="targy"/></td>
                            <td><xsl:value-of select="idopont/nap"/></td>
                            <td>
                                <xsl:value-of select="idopont/tol"/>
                                <xsl:text>–</xsl:text>
                                <xsl:value-of select="idopont/ig"/>
                            </td>
                            <td><xsl:value-of select="helyszin"/></td>
                            <td><xsl:value-of select="oktato"/></td>
                            <td><xsl:value-of select="szak"/></td>
                        </tr>
                    </xsl:for-each>

                </table>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
