<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl">
    <xsl:output method="xml" indent="yes"/>
    <xsl:template match="/">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4" page-height="29.7cm" page-width="21.0cm" margin-top="1cm" margin-left="2cm" margin-right="2cm" margin-bottom="1cm">
                    <!-- Page template goes here -->
                    <fo:region-body />
                    <fo:region-before region-name="xsl-region-before" extent="3cm"/>
                    <fo:region-after region-name="xsl-region-after" extent="4cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="A4">
                <!-- Page content goes here -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block>
                        <fo:table>
                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-column column-width="8.5cm"/>
                            <fo:table-body>
                                <fo:table-row font-size="16pt" line-height="25px" background-color="#d94858" color="white">
                                    <fo:table-cell padding-left="5pt" padding-right="5pt">
                                        <fo:block>
                                            Increff POS
                                        </fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell padding-right="5pt">
                                        <fo:block text-align="right">
                                            INVOICE
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body" line-height="20pt">
                    <xsl:apply-templates />
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="invoice">
        <fo:block></fo:block>
        <fo:block space-before="35pt" width="17cm" >
            <fo:table>
                <fo:table-column column-width="5.5cm"/>
                <fo:table-column column-width="5.5cm"/>
                <fo:table-body>
                    <fo:table-row>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <fo:inline font-weight="bold" font-size="11pt">Invoice Number</fo:inline>&#x2028;
                                <fo:inline font-weight="bold" font-size="11pt">Invoice Date</fo:inline>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                        <fo:table-cell>
                            <fo:block text-align="left">
                                <xsl:value-of select="./order_id"></xsl:value-of>&#x2028;
                                <xsl:value-of select="./order_date"></xsl:value-of>&#x2028;
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
        <fo:block space-before="35pt">
            <fo:table line-height="25px" overflow="hidden">
                <fo:table-column column-width="1.6cm"/>
                <fo:table-column column-width="6.5cm"/>
                <fo:table-column column-width="2cm"/>
                <fo:table-column column-width="2.9cm"/>
                <fo:table-column column-width="4cm"/>
                <fo:table-header>
                    <fo:table-row background-color="#f5f5f5" text-align="center" font-weight="bold">
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block font-size="11pt">S No.</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block-container overflow="hidden" linefeed-treatment="preserve">
                                <fo:block font-size="11pt">Name</fo:block>
                            </fo:block-container>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block font-size="11pt">Quantity</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block font-size="11pt">Unit Price</fo:block>
                        </fo:table-cell>
                        <fo:table-cell border="1px solid #b8b6b6">
                            <fo:block font-size="11pt">Total</fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-header>
                <fo:table-body>
                    <xsl:apply-templates select="order_item"></xsl:apply-templates>


                    <fo:table-row font-weight="bold">
                        <fo:table-cell number-columns-spanned="4" text-align="right" padding-right="3pt">
                            <fo:block>Total</fo:block>
                        </fo:table-cell>
                        <fo:table-cell  text-align="right" padding-right="3pt" background-color="#f5f5f5" border="1px solid #b8b6b6" >
                            <fo:block font-size="10pt">
                                <xsl:value-of select="amount" />
                            </fo:block>
                        </fo:table-cell>
                    </fo:table-row>
                </fo:table-body>
            </fo:table>
        </fo:block>
    </xsl:template>

    <xsl:template match="order_item">
        <fo:table-row>
            <fo:table-cell border="1px solid #b8b6b6" padding-left="3pt">
                <fo:block font-size="10pt">
                    <xsl:value-of select="id"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" padding-left="3pt" padding-right="3pt">
                <fo:block-container width="100%" overflow="hidden">
                    <fo:block>
                        <fo:table table-layout="fixed" width="100%">
                            <fo:table-column column-width="100%"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell>
                                        <fo:block font-size="10pt">
                                            <xsl:call-template name="wrapText">
                                                <xsl:with-param name="text" select="product_name"/>
                                            </xsl:call-template>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:block-container>
            </fo:table-cell>

            <fo:table-cell border="1px solid #b8b6b6" text-align="center">
                <fo:block font-size="10pt">
                    <xsl:value-of select="quantity"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block font-size="10pt">
                    <xsl:value-of select="selling_price"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell border="1px solid #b8b6b6" text-align="right" padding-right="3pt">
                <fo:block font-size="10pt">
                    <xsl:value-of select="amt"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>

    </xsl:template>

    <xsl:template name="wrapText">
        <xsl:param name="text"/>
        <fo:block linefeed-treatment="preserve">
            <xsl:choose>
                <xsl:when test="string-length($text) &gt; 26">
                    <xsl:value-of select="substring($text, 1, 26)"/>
                    <xsl:text>&#45;&#10;</xsl:text>
                    <xsl:call-template name="wrapText">
                        <xsl:with-param name="text" select="substring($text, 27)"/>
                    </xsl:call-template>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$text"/>
                </xsl:otherwise>
            </xsl:choose>
        </fo:block>
    </xsl:template>
</xsl:stylesheet>