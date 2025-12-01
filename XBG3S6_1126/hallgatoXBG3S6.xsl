<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Hallgatók adatai</title>
                <style>
                    body { font-family: Arial; margin: 20px; }
                    h2 { color: #6e2b8b; }
                    table { border-collapse: collapse; }
                    th, td { border: 1px solid #6e2b8b; padding: 4px 8px; }
                    th { background-color: #caa3d8; }
                    td { background-color: #e6c9f0; }
                </style>
            </head>
            <body>

                <h2>Hallgatók adatai – for-each, value-of</h2>

                <table>
                    <tr>
                        <th>ID</th>
                        <th>Vezetéknév</th>
                        <th>Keresztnév</th>
                        <th>Becenév</th>
                        <th>Kor</th>
                        <th>Fizetés</th>
                    </tr>

                    <xsl:for-each select="class/student">
                        <tr>
                            <td><xsl:value-of select="@id"/></td>
                            <td><xsl:value-of select="vezeteknev"/></td>
                            <td><xsl:value-of select="keresztnev"/></td>
                            <td><xsl:value-of select="becenev"/></td>
                            <td><xsl:value-of select="kor"/></td>
                            <td><xsl:value-of select="osztondij"/></td>
                        </tr>
                    </xsl:for-each>

                </table>

            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
