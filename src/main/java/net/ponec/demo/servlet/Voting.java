/*
 * Copyright 2020-2021 Pavel Ponec, https://github.com/pponec/demo-ajax
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ponec.demo.servlet;

import net.ponec.demo.model.ButtonModel;
import org.jetbrains.annotations.NotNull;
import org.ujorm.tools.web.Element;
import org.ujorm.tools.web.Html;
import org.ujorm.tools.web.HtmlElement;
import org.ujorm.tools.web.ajax.JavaScriptWriter;
import org.ujorm.tools.web.ao.HttpParameter;
import org.ujorm.tools.web.json.JsonBuilder;
import org.ujorm.tools.xml.config.HtmlConfig;
import org.ujorm.tools.xml.config.impl.DefaultHtmlConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static net.ponec.demo.servlet.Voting.Attrib.VOTEBUTTON;
import static net.ponec.demo.servlet.Voting.Attrib.STATUS;
import static net.ponec.demo.servlet.Voting.Constants.*;
import static org.ujorm.tools.web.ajax.JavaScriptWriter.DEFAULT_AJAX_REQUEST_PARAM;

/**
 * A live example of the HtmlElement inside a Servlet using a ujo-web library.
 *
 * @author Pavel Ponec
 * @see <a href=https://github.com/pponec/demo-ajax">github.com/pponec/demo-ajax</a>
 */
@WebServlet({"", "/seven-voting"})
public class Voting extends HttpServlet {
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(Voting.class.getName());

    /** CSS button style */
    private static final String BTN_STYLE = "btn";

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param input servlet request
     * @param output servlet response
     */
    @Override
    protected void doGet(
            final HttpServletRequest input,
            final HttpServletResponse output) {

        try (HtmlElement html = HtmlElement.of(input, output, getConfig("Seven Voting Points"))) {
            html.addCssLink("/css/regexp.css");
            writeJavaScript(html, AJAX_ENABLED);
            ButtonModel model = getModel(input);
            try (Element body = html.addBody()) {
                body.addHeading(html.getTitle());
                body.addDiv(SUBTITLE_CSS).addText(AJAX_ENABLED ? AJAX_READY_MSG : "");
                try (Element form = body.addForm().setMethod(Html.V_POST).setAction("?")) {
                    createFormBody(form, model);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Internal server error", e);
            output.setStatus(500);
        }
    }

    @NotNull
    private ButtonModel getModel(HttpServletRequest input) {
        return ButtonModel.of(
                STATUS.of(input, 0),
                VOTEBUTTON.of(input, -1));
    }

    private void createFormBody(Element form, ButtonModel msg) {
        try (Element buttonBar = form.addDiv()) {
            addButton(buttonBar, 0, msg, "ThumbDown-1294983.svg", "DOWN", 50);
            addButton(buttonBar, 1, msg, "ThumbDown-1294983.svg", "Down", 30);
            addButton(buttonBar, 2, msg, "ThumbUp-1294983.svg", "Up", 30);
            addButton(buttonBar, 3, msg, "ThumbUp-1294983.svg", "UP", 50);
        }
        try (Element result = form.addDiv("result")) {
            result.addLabel().addText("Result:");
            result.addSpan(CONTROL_CSS, OUTPUT_CSS).addText(msg);
        }
        form.addInput().setType(Html.V_HIDDEN).setNameValue(STATUS, msg);
    }

    private void addButton(Element e, int buttonIndex, ButtonModel model, String file, String alt, int width) {
        CharSequence[] css = {BTN_STYLE,  model.isEnabled(buttonIndex) ? "" : "grayscale"};
        e.addSubmitButton(css)
                .setNameValue(VOTEBUTTON, buttonIndex)
                .addImage("images/" + file, alt)
                .setAttribute(Html.A_PLACEHOLDER, alt)
                .setAttribute("width", width)
                .setAttribute("height", width);
    }

    private HtmlConfig getConfig(String title) {
        DefaultHtmlConfig result = HtmlConfig.ofDefault();
        result.setTitle(title);
        result.setNiceFormat("\t");
        return  result;
    }

    @Override
    protected void doPost(HttpServletRequest input, HttpServletResponse output)
            throws IOException {
        if (DEFAULT_AJAX_REQUEST_PARAM.of(input, false)) {
            doAjax(input, JsonBuilder.of(getConfig("?"), input, output)).close();
        } else {
            doGet(input, output);
        }
    }

    @NotNull
    protected JsonBuilder doAjax(HttpServletRequest input, JsonBuilder output)
            throws IOException {
            final ButtonModel model = getModel(input);
            output.write(Html.FORM, e -> createFormBody(e, model));
            output.writeClass(SUBTITLE_CSS, AJAX_READY_MSG);
            return output;
    }

    /** Write a Javascript to a header */
    protected void writeJavaScript(@NotNull final HtmlElement html, final boolean enabled) {
        if (enabled) {
            new JavaScriptWriter(Html.INPUT, Html.TEXT_AREA)
                    .setSubtitleSelector("." + SUBTITLE_CSS)
                    .write(html.getHead());
        }
    }

    /** CSS constants and identifiers */
    static class Constants {
        /** Bootstrap form control CSS class name */
        static final String CONTROL_CSS = "form-control";
        /** CSS class name for the output box */
        static final String OUTPUT_CSS = "out";
        /** CSS class name for the output box */
        static final String SUBTITLE_CSS = "subtitle";
        /** Enable AJAX feature */
        static final boolean AJAX_ENABLED = true;
        /** AJAX ready message */
        static final String AJAX_READY_MSG = "AJAX ready";
    }

    /** Servlet attributes */
    enum Attrib implements HttpParameter {
        VOTEBUTTON,
        STATUS;

        @Override @NotNull
        public String toString() {
            return name().toLowerCase();
        }
    }
}
