package com.microsoft.fluentuidemo.demos

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.microsoft.fluentui.tokenized.controls.Button
import com.microsoft.fluentui.tokenized.divider.Divider
import com.microsoft.fluentui.tokenized.listitem.ListItem
import com.microsoft.fluentui.tokenized.notification.ToolTipBox
import com.microsoft.fluentui.tokenized.notification.rememberTooltipState
import com.microsoft.fluentui.util.isAccessibilityEnabled
import com.microsoft.fluentuidemo.DemoActivity
import com.microsoft.fluentuidemo.R
import com.microsoft.fluentuidemo.V2DemoActivity
import com.microsoft.fluentuidemo.databinding.V2ActivityComposeBinding
import com.microsoft.fluentuidemo.util.getAndroidViewAsContent
import com.microsoft.fluentuidemo.util.invokeToast
import kotlinx.coroutines.launch

class V2ToolTipActivity : V2DemoActivity() {

    init {
        setupActivity(this)
    }

    override val paramsUrl = "https://github.com/microsoft/fluentui-android/wiki/Controls#params-35"
    override val controlTokensUrl = "https://github.com/microsoft/fluentui-android/wiki/Controls#control-tokens-34"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityContent {
            CreateToolTipActivityUI(this)
        }
    }
}
val TOOLTIP_ACTIVITY_X_OFFSET_TEXTFIELD_TAG = "xOffset"
val TOOLTIP_ACTIVITY_Y_OFFSET_TEXTFIELD_TAG = "yOffset"

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateToolTipActivityUI(context: Context) {
    val xOffsetState = rememberSaveable { mutableStateOf("0") }
    val yOffsetState = rememberSaveable { mutableStateOf("0") }
    val toolTipTitle =
        rememberSaveable { mutableStateOf(context.getString(R.string.tooltip_title)) }
    val toolTipText =
        rememberSaveable { mutableStateOf(context.getString(R.string.tooltip_text)) }

    val xOffset =
        if (xOffsetState.value.toFloatOrNull() == null) 0.dp else xOffsetState.value.toFloat().dp
    val yOffset =
        if (yOffsetState.value.toFloatOrNull() == null) 0.dp else yOffsetState.value.toFloat().dp

    val action = stringResource(id = R.string.tooltip_dismiss_message)
    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .semantics {
                testTagsAsResourceId = true
            }
    ) {
        //Top
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Top Left
            val topLeftTooltipState = rememberTooltipState()
            val topStartString = stringResource(id = R.string.tooltip_top_start)
            ToolTipBox(
                title = toolTipTitle.value,
                text = toolTipText.value,
                tooltipState = topLeftTooltipState,
                offset = DpOffset(x = xOffset, y = yOffset),
                focusable = context.isAccessibilityEnabled,
                onDismissRequest = { invokeToast(topStartString, context, action) }) {
                Button(
                    onClick = { scope.launch { topLeftTooltipState.show() } },
                    text = stringResource(id = R.string.tooltip_button),
                    modifier = Modifier.testTag(topStartString)
                )
            }

            //Top Right
            val topRightTooltipState = rememberTooltipState()
            val topEndString = stringResource(id = R.string.tooltip_top_end)
            ToolTipBox(
                title = toolTipTitle.value,
                text = toolTipText.value,
                tooltipState = topRightTooltipState,
                offset = DpOffset(x = xOffset, y = yOffset),
                focusable = context.isAccessibilityEnabled,
                onDismissRequest = { invokeToast(topEndString, context, action) }) {
                Button(
                    onClick = { scope.launch { topRightTooltipState.show() } },
                    text = stringResource(id = R.string.tooltip_button),
                    modifier = Modifier.testTag(topEndString)
                )
            }
        }
        //Config

        Column {
            ListItem.Header(title = context.getString(R.string.menu_xOffset),
                trailingAccessoryContent = {
                    BasicTextField(value = xOffsetState.value,
                        modifier = Modifier.testTag(TOOLTIP_ACTIVITY_X_OFFSET_TEXTFIELD_TAG),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { xOffsetState.value = it.trim() })
                }
            )
            ListItem.Header(title = context.getString(R.string.menu_yOffset),
                trailingAccessoryContent = {
                    BasicTextField(value = yOffsetState.value,
                        modifier = Modifier.testTag(TOOLTIP_ACTIVITY_Y_OFFSET_TEXTFIELD_TAG),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = { yOffsetState.value = it.trim() })
                })
            ListItem.Header(title = context.getString(R.string.tooltip_title),
                trailingAccessoryContent = {
                    BasicTextField(
                        value = toolTipTitle.value,
                        onValueChange = { toolTipTitle.value = it })
                })
            ListItem.Header(title = context.getString(R.string.tooltip_text),
                trailingAccessoryContent = {
                    BasicTextField(
                        value = toolTipText.value,
                        onValueChange = { toolTipText.value = it })
                })
            Divider()
        }
        //Center with Customized Content
        val centerCustomizedTooltipState = rememberTooltipState()
        val customCenterString = stringResource(id = R.string.tooltip_custom_center)
        ToolTipBox(
            tooltipContent = {
                getAndroidViewAsContent(ContentType.WRAPPED_SIZE_CONTENT)() {}
            },
            tooltipState = centerCustomizedTooltipState,
            offset = DpOffset(x = xOffset, y = yOffset),
            focusable = context.isAccessibilityEnabled,
            onDismissRequest = {
                invokeToast(
                    customCenterString,
                    context,
                    action
                )
            }) {
            Button(
                onClick = { scope.launch { centerCustomizedTooltipState.show() } },
                text = stringResource(id = R.string.tooltip_custom_content),
                modifier = Modifier.testTag(customCenterString)
            )
        }

        val centerTooltipState = rememberTooltipState()
        val centerString = stringResource(id = R.string.tooltip_center)
        ToolTipBox(
            title = toolTipTitle.value,
            text = toolTipText.value,
            tooltipState = centerTooltipState,
            offset = DpOffset(x = xOffset, y = yOffset),
            focusable = context.isAccessibilityEnabled,
            onDismissRequest = { invokeToast(centerString, context, action) }) {
            Button(
                onClick = { scope.launch { centerTooltipState.show() } },
                text = stringResource(id = R.string.tooltip_button),
                modifier = Modifier.testTag(centerString)
            )
        }

        //Bottom
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            //Bottom Left
            val bottomLeftTooltipState = rememberTooltipState()
            val bottomStartString = stringResource(id = R.string.tooltip_bottom_start)
            ToolTipBox(
                title = toolTipTitle.value,
                text = toolTipText.value,
                tooltipState = bottomLeftTooltipState,
                offset = DpOffset(x = xOffset, y = yOffset),
                focusable = context.isAccessibilityEnabled,
                onDismissRequest = { invokeToast(bottomStartString, context, action) }) {
                Button(
                    onClick = { scope.launch { bottomLeftTooltipState.show() } },
                    text = stringResource(id = R.string.tooltip_button),
                    modifier = Modifier.testTag(bottomStartString)
                )
            }

            //Bottom Right
            val bottomRightTooltipState = rememberTooltipState()
            val bottomEndString = stringResource(id = R.string.tooltip_bottom_end)
            ToolTipBox(
                title = toolTipTitle.value,
                text = toolTipText.value,
                tooltipState = bottomRightTooltipState,
                offset = DpOffset(x = xOffset, y = yOffset),
                focusable = context.isAccessibilityEnabled,
                onDismissRequest = { invokeToast(bottomEndString, context, action) }) {
                Button(
                    onClick = { scope.launch { bottomRightTooltipState.show() } },
                    text = stringResource(id = R.string.tooltip_button),
                    modifier = Modifier.testTag(bottomEndString)
                )
            }
        }
    }
}
