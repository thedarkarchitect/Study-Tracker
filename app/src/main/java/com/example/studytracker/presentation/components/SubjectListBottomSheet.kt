package com.example.studytracker.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.studytracker.domain.model.Subject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectListBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    isOpen: Boolean,
    subjects: List<Subject>,
    bottomSheetTitle: String = "Related to subject",
    onSubjectClicked: (Subject) -> Unit,
    onDismissRequest: () -> Unit
) {
    if(isOpen){
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismissRequest,
            dragHandle = {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BottomSheetDefaults.DragHandle()
                    Text(text = bottomSheetTitle)
                    Spacer(modifier = modifier.height(10.dp))
                    HorizontalDivider()
                }
            }
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp)
            ) {
                items(subjects) { subject ->
                    Box(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable { onSubjectClicked(subject) } //the specific subject clicked is passed to the click event to handle it // by it id arg
                            .padding(8.dp)
                    ) {
                        Text(text = subject.name)
                    }
                }
                if (subjects.isEmpty()) {
                    item {
                        Text(
                            modifier = modifier.padding(10.dp),
                            text = "Ready to begin? First, add a subject."
                        )
                    }
                }
            }
        }
    }
}