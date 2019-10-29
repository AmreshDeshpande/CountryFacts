package com.cognizant.facts.feature.di

import com.cognizant.facts.feature.ui.FactsHomeFragment
import dagger.Component

@Component(modules = [FactsModule::class])
interface FactsComponent {
    fun inject(factsHomeFragment: FactsHomeFragment)
}