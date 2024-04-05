package org.d3if3026.assesment1.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    placeholder: @Composable (() -> Unit)? = null,
) {
    OutlinedTextField(
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label,
        leadingIcon = if (leadingIcon != null) { { leadingIcon() } } else null,
        trailingIcon = if (trailingIcon != null) { { trailingIcon() } } else null,
        supportingText = supportingText,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(30.dp),
        placeholder = placeholder,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = if (isError) {
                Color(0xFFD32F2F)
            } else {
                Color.Gray
            },
            focusedBorderColor = if (isError) {
                Color(0xFFD32F2F)
            } else {
                Color.Gray
            },
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginTextField() {
    LoginTextField(
        value = "Username",
        onValueChange = {},
        modifier = Modifier,
        label = { },
        supportingText = { },
        isError = false,
        visualTransformation = VisualTransformation.None,
        keyboardOptions = KeyboardOptions.Default,
    )
}