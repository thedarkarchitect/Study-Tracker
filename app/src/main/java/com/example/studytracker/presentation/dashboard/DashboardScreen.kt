package com.example.studytracker.presentation.dashboard


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studytracker.R
import com.example.studytracker.domain.model.Subject
import com.example.studytracker.presentation.components.CountCard
import com.example.studytracker.presentation.components.SubjectCard
import com.example.studytracker.ui.theme.StudyTrackerTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {

    val subjects = listOf(
        Subject(name = "Art", goalHours = 10f, colors = Subject.subjectCardColors.random()),
        Subject(name = "Literature", goalHours = 10f, colors = Subject.subjectCardColors.random()),
        Subject(name = "Geography", goalHours = 10f, colors = Subject.subjectCardColors.random()),
        Subject(name = "Mathematics", goalHours = 10f, colors = Subject.subjectCardColors.random()),
        Subject(name = "Biology", goalHours = 10f, colors = Subject.subjectCardColors.random()),
        Subject(name = "Business", goalHours = 10f, colors = Subject.subjectCardColors.random())
    )

    Scaffold(
        topBar = { DashboardTopBar() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){

            item {
                CountCardsSection(
                    subjectCount = 5,
                    studiedHours = "10",
                    goalHours = "15"
                )
            }

//            item {
//                SubjectCardsSection(
//                    modifier = modifier.fillMaxWidth(),
//                    subjectList = emptyList()
//                )
//            }

            item {
                SubjectCardsSection(
                    modifier = modifier.fillMaxWidth(),
                    subjectList = subjects
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.studytracker),
                style = MaterialTheme.typography.displaySmall
            )
        }
    )
}

@Composable
fun CountCardsSection(
    modifier: Modifier = Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ){
        CountCard(
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = modifier.width(10.dp))
        CountCard(
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = modifier.width(10.dp))
        CountCard(
            headingText = "Goals Study Hours",
            count = goalHours
        )
    }
}

@Composable
fun SubjectCardsSection(
    modifier: Modifier = Modifier,
    subjectList: List<Subject>,
    emptyListText: String = stringResource(R.string.empty_subject)
) {
    Column{
        //always shown
        Row (
            modifier = modifier.padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.subjects),
                style = MaterialTheme.typography.bodySmall,
            )

            IconButton(
                onClick = {}
            ) {
               Icon(
                   imageVector = Icons.Default.Add,
                   contentDescription = stringResource(R.string.add_subject)
               )
            }
        }

        //this appears on condition
        if (subjectList.isEmpty()){
            Image(
                modifier = modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 4.dp, bottom = 8.dp),
                painter = painterResource(id = R.drawable.books),
                contentDescription = stringResource(R.string.empty_subject)
            )
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(R.string.empty_subject),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(subjectList){
                SubjectCard(
                    subjectName = it.name,
                    gradientColors = it.colors,
                    onClick = { TODO() }
                )
            }
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Preview(apiLevel = 33, showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashBoardPreview() {
    StudyTrackerTheme {
        DashboardScreen()
    }
}

