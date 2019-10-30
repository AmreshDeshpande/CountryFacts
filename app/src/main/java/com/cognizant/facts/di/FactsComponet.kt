package com.cognizant.facts.di

import com.cognizant.facts.ui.FactsHomeFragment
import dagger.Component

@Component(modules = [FactsModule::class])
interface FactsComponent {
    fun inject(factsHomeFragment: FactsHomeFragment)
}