package nz.ac.canterbury.seng303.ass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nz.ac.canterbury.seng303.ass.screens.CardList
import nz.ac.canterbury.seng303.ass.screens.CreateFlashCard
import nz.ac.canterbury.seng303.ass.screens.EditCard
import nz.ac.canterbury.seng303.ass.screens.FlashCard
import nz.ac.canterbury.seng303.ass.ui.theme.Lab1Theme
import nz.ac.canterbury.seng303.ass.viewmodels.CreateCardViewModel
import nz.ac.canterbury.seng303.ass.viewmodels.EditCardViewModel
import nz.ac.canterbury.seng303.ass.viewmodels.FlashCardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel as koinViewModel

class MainActivity : ComponentActivity() {

    private val cardViewModel: FlashCardViewModel by koinViewModel()
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cardViewModel.loadDefaultCards()

        setContent {
            Lab1Theme {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        // Add your AppBar content here
                        TopAppBar(
                            title = { Text("SENG303 Lab 2") },
                            navigationIcon = {
                                IconButton(onClick = { navController.popBackStack() }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    }
                ) {

                    Box(modifier = Modifier.padding(it)) {
                        val createCardViewModel: CreateCardViewModel = viewModel()
                        val editCardViewModel: EditCardViewModel = viewModel()
                        NavHost(navController = navController, startDestination = "Home") {
                            composable("Home") {
                                Home(navController = navController)
                            }
                            composable(
                                "FlashCard/{cardId}",
                                arguments = listOf(navArgument("cardId") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let { cardIdParam: String -> FlashCard(cardIdParam, cardViewModel) }
                            }
                            composable("CardList") {
                                CardList(navController, cardViewModel)
                            }
                            composable("CreateCard") {
                                CreateFlashCard(
                                    navController = navController,
                                    question = createCardViewModel.question ,
                                    onQuestionChange = { newQuestion ->
                                        createCardViewModel.updateQuestion(newQuestion)
                                    },
                                    answers = createCardViewModel.answers,
                                    onAnswersChange = { newAnswer ->
                                        createCardViewModel.updateAnswers(newAnswer)
                                    },
                                    createFlashCardFn = {question, answers -> cardViewModel.createCard(question, answers)}
                                )
                            }
                            composable("EditCard/{cardId}", arguments = listOf(navArgument("cardId") {
                                type = NavType.StringType
                            })
                            ) { backStackEntry ->
                                val cardId = backStackEntry.arguments?.getString("cardId")
                                cardId?.let { cardIdParam: String -> EditCard(cardIdParam, editCardViewModel, cardViewModel, navController = navController) }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("CardList") }) {
            Text(text = "Card List")
        }
        
        Button(onClick = { navController.navigate("CreateCard") }) {
            Text(text = "Create Flash Card")
        }
    }
}
