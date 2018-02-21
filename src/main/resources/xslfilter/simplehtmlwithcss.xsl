<?xml version="1.0"?>
<!--
  Copyright 2004 Guy Van den Broeck

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes"/>

    <xsl:template match="/">
        <html>
            <head>
                <xsl:apply-templates select="diffreport/css/node()"/>
                <style type="text/css">

                    span.diff-tag-html {
                    font-family: "Andale Mono" monospace;
                    font-size: 80%;
                    }

                    span.diff-tag-removed {
                    font-size: 100%;
                    text-decoration: line-through;
                    background-color: #fdc6c6; /* light red */
                    }

                    span.diff-tag-added {
                    font-size: 100%;
                    background-color: #ccffcc; /* light green */
                    }

                    span.diff-tag-conflict {
                    font-size: 100%;
                    background-color: #f781be; /* light rose */
                    }

                    /*
                    * Styles for the HTML Diff
                    */
                    span.diff-html-added {
                    font-size: 100%;
                    background-color: #ccffcc; /* light green */
                    cursor: pointer;
                    }

                    span.diff-html-removed {
                    font-size: 100%;
                    text-decoration: line-through;
                    background-color: #fdc6c6; /* light red */
                    cursor: pointer;
                    }

                    span.diff-html-changed {
                    background: url(../images/diffunderline.gif) bottom repeat-x;
                    *background-color: #c6c6fd; /* light blue */
                    cursor: pointer;
                    }

                    span.diff-html-conflict {
                    /* background: url(../images/diffunderline.gif) bottom repeat-x; */
                    background-color: #f781be; /* light rose */
                    }

                    span.diff-html-selected {
                    background-color: #FF8800; /* light orange */
                    cursor: pointer;
                    }

                    span.diff-html-selected img{
                    border: 2px solid #FF8800; /* light orange */
                    }

                    span.diff-html-added img{
                    border: 2px solid #ccffcc;
                    }

                    span.diff-html-removed img{
                    border: 2px solid #fdc6c6;
                    }

                    span.diff-html-changed img{
                    border: 2px dotted #000099;

                    }

                    div.diff-removed-image, div.diff-added-image, div.diff-conflict-image {
                    height: 300px;
                    width: 200px;
                    position: absolute;
                    opacity : 0.55;
                    filter: alpha(opacity=55);
                    -moz-opacity: 0.55;
                    }

                    div.diff-removed-image, div.diff-added-image, div.diff-conflict-image {
                    margin-top: 2px;
                    margin-bottom: 2px;
                    margin-right: 2px;
                    margin-left: 2px;
                    }


                    table.diff-tooltip-link, table.diff-tooltip-link-changed {
                    width: 100%;
                    text-align: center;
                    Vertical-align: middle;
                    }

                    table.diff-tooltip-link-changed {
                    border-top: thin dashed #000000;
                    margin-top: 3px;
                    padding-top: 3px
                    }
                    td.diff-tooltip-prev {
                    text-align: left;
                    }

                    td.diff-tooltip-next {
                    text-align: right;
                    }

                    table.diffpage-html-firstlast {
                    width: 100%;
                    Vertical-align: middle;
                    }

                    div.diff-topbar{
                    border-bottom: 2px solid #FF8800;
                    border-left: 1px solid #FF8800;
                    border-right: 1px solid #FF8800;
                    background-color: #FFF5F5;
                    }

                    a.diffpage-html-a, a.diffpage-html-a:hover, a.diffpage-html-a:link, a.diffpage-html-a:visited,
                    a.diffpage-html-a:active {
                    text-decoration: none;
                    color: #FF8800;
                    }

                    .diffpage-html-firstlast a img, .dsydiff-prevnextnav a img {
                    vertical-align: middle;
                    }

                    ul.changelist {
                    padding-left: 15px;
                    }

                    body{
                    margin-top: 0px;
                    }

                </style>
            </head>
            <body>

                <xsl:apply-templates select="diffreport/diff/node()"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>