package com.example.thirtydays

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thirtydays.model.Motorcycle
import com.example.thirtydays.ui.theme.ThirtyDaysTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MotorcyclesList(
    motorcycles: List<Motorcycle>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val visibleState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    // Fade in entry animation  for the entire list
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(
            animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
        ),
        exit = fadeOut(),
        modifier = modifier
    ) {
        LazyColumn(contentPadding = contentPadding) {
            itemsIndexed(motorcycles) { index, motorcycle ->
                MotorcycleItem(
                    motorcycle = motorcycle,
                    day = index + 1,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        // Animate each list item to slide in vertically
                        .animateEnterExit(
                            enter = slideInVertically(
                                animationSpec = spring(
                                    stiffness = StiffnessVeryLow,
                                    dampingRatio = DampingRatioLowBouncy
                                ),
                                initialOffsetY = { it * index + 1 } // staggered entrance
                            )
                        )
                )
            }
        }
    }

}

@Composable
fun MotorcycleItem(
    motorcycle: Motorcycle,
    day: Int,
    modifier: Modifier = Modifier
) {

    //  initially ExpandLess
    var expanded by remember { mutableStateOf(false) }

    // the Card Composable function
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier // inter-card padding passed in
    ) {
        // the animation spec to make the expand more/less a smooth transition
        Column(
            modifier = Modifier
                .padding(16.dp) // intra-card padding
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            // first item is the "Day n" label
            DayLabel(day)

            // second item is row of icon button, make and image
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 72.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MotorcycleQuoteButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
                MotorcycleMake(motorcycle)
                Spacer(Modifier.weight(1f))
                MotorcycleImage(motorcycle)
            }

            // third item is the quote to expand more/less
            if (expanded) {
                MotorcycleQuote(motorcycle, modifier = Modifier.padding(top = 16.dp))
            }
        }
    }
}

@Composable
fun DayLabel(day: Int = 1) {
    Text(
        text = stringResource(R.string.day_template).format(day),
        style = MaterialTheme.typography.displayLarge
    )
}

@Composable
fun MotorcycleMake(motorcycle: Motorcycle) {
    Text(
        text = stringResource(motorcycle.makeRes),
        style = MaterialTheme.typography.displayMedium
    )
}

@Composable
fun MotorcycleQuoteButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        // align chevron with day label on left margin
        modifier = modifier.width(25.dp)
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun MotorcycleQuote(motorcycle: Motorcycle, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(motorcycle.quoteRes),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun MotorcycleImage(motorcycle: Motorcycle) {
    Box(
        modifier = Modifier
            .sizeIn(72.dp, 72.dp, 72.dp, 72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(motorcycle.imageRes),
            contentDescription = stringResource(motorcycle.makeRes),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MotorCycleMakePreview() {
    val previewMotorcycle =
        Motorcycle(R.string.make1, R.string.quote1, R.drawable.icons_sportbike_72)
    var previewExpanded by remember { mutableStateOf(false) }
    val previewDay = 1
    ThirtyDaysTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.surface) { //  temporary container to preview
//            DayLabel(15)
//            MotorcycleMake(previewMotorcycle)
//            MotorcycleQuote(previewMotorcycle)
//            MotorcycleImage(previewMotorcycle)
//            MotorcycleQuote(previewMotorcycle)
//            MotorcycleQuoteButton(
//                expanded = previewExpanded,
//                onClick = { previewExpanded = !previewExpanded }
//            )
            MotorcycleItem(
                previewMotorcycle,
                previewDay,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ) // inter-card padding
            )

//            MotorcyclesList(motorcycles = MotorcycleRepository.motorcycles)


        }
    }
}