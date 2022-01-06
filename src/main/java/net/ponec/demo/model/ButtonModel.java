/*
 * Copyright 2020-2021 Pavel Ponec, https://github.com/pponec
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

package net.ponec.demo.model;

import net.ponec.demo.service.Converter;

/**
 *
 * @author Pavel Ponec
 */
public class ButtonModel {

    /** A converter */
    private final Converter converter = new Converter();

    /** Button is enabled to click (has a color) */
    private boolean[] enabled;

    protected ButtonModel(int status) {
        enabled = init(status);
    }

    protected final boolean[] initOfIdx(final int idx) {
        return init(converter.index2id(idx));
    }

    protected boolean[] init(final int status) {
        final boolean[] result = {true, true, true, true};
        switch (status) {
            case -3:
                result[0] = false;
                result[1] = false;
                break;
            case -2:
                result[0] = false;
               break;
            case -1:
                result[1] = false;
                break;
            case 1:
                result[2] = false;
                break;
            case 2:
                result[3] = false;
                break;
            case 3:
                result[2] = false;
                result[3] = false;
                break;
        }
        return result;
    }

    /** Switch off the button */
    public void buttonClick(int idx) {
        if (idx < 0) {
            return;
        }
        if (!enabled[idx]) {
            enabled = init(0);
            return;
        }
        switch (idx) {
            case 0:
            case 3:
                enabled = initOfIdx(idx);
                break;
            case 1:
                if (!enabled[idx - 1]) {
                    enabled[idx] = false;
                } else {
                    enabled = initOfIdx(idx);
                }
                break;
            case 2:
                if (!enabled[idx + 1]) {
                    enabled[idx] = false;
                } else {
                    enabled = initOfIdx(idx);
                }
                break;
        }
    }

    /** Check if the button is enabled on the required position */
    public boolean isEnabled(int idx) {
        return enabled[idx];
    }


    public int getStatus() {
        int result = 0;
        for (int i = 0; i < enabled.length; i++) {
            if (!enabled[i]) {
                result += converter.index2id(i);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(getStatus());
    }

    public static ButtonModel of(int model, int buttonIndex) {
        final ButtonModel result = new ButtonModel(model);
        result.buttonClick(buttonIndex);
        return result;
    }
}
