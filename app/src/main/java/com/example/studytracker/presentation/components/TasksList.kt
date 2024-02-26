package com.example.studytracker.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.domain.model.Task
import com.example.studytracker.util.Priority
import com.example.studytracker.util.changeMillisToDateString

fun LazyListScope.tasksList(
    modifier: Modifier = Modifier,
    sectionTitle: String,
    emptyTaskText: String,
    tasks: List<Task>,
    onTaskCardClick: (Int?) -> Unit, //task card use ints which will be id to link to specific tasks
    onCheckBoxClick: (Task) -> Unit//takes task because the click controls boolean value that also in task model can be used to show if task is complete
) {
    item {
        Row(
            modifier = modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = sectionTitle,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
    if(tasks.isEmpty()){
        item {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = modifier.size(120.dp),
                    painter = painterResource(id = R.drawable.checklist),
                    contentDescription = emptyTaskText
                )
                Spacer(modifier = modifier.height(12.dp))
                Text(
                    text = emptyTaskText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    items(tasks){task ->
        TaskCard(
            modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            task = task,
            onCheckBoxClick = { onCheckBoxClick(task) },
            onClick = { onTaskCardClick(task.taskId) }
        )
    }
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCheckBoxClick: () -> Unit,
    onClick: () -> Unit
) {

    ElevatedCard(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = task.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isComplete) TextDecoration.LineThrough else TextDecoration.None
                )
                Spacer(modifier = modifier.height(2.dp))
                Text(
                    text = task.dueDate.changeMillisToDateString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            TaskCheckBox(
                isComplete = task.isComplete,
                borderColor = Priority.fromInt(task.priority).color,//using the priority enum to color checkbox dynamically
                onCheckBoxClick = onCheckBoxClick
            )
        }
    }
}