package com.example.dnd_manager.theme;

/**
 * TextField that supports only integer input.
 */
public class IntegerField extends AppTextField {

    public IntegerField(String prompt) {
        super(prompt);
        allowOnlyInteger();
    }

    /**
     * Returns integer value.
     *
     * @return integer value
     */
    public int getValue() {
        return getInt();
    }

    /**
     * Sets integer value to the field.
     *
     * @param value integer value
     */
    public void setValue(int value) {
        setText(String.valueOf(value));
    }
}
