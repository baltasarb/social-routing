package ps.g49.socialroutingclient.dagger.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import ps.g49.socialroutingclient.viewModel.UserProfileViewModel
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.dagger.factory.ViewModelKey
import ps.g49.socialroutingclient.viewModel.GoogleViewModel

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SocialRoutingViewModel::class)
    protected abstract fun socialRoutingViewModel(socialRoutingViewModel: SocialRoutingViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel::class)
    protected abstract fun userProfileViewModel(userProfileViewModel: UserProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoogleViewModel::class)
    protected abstract fun googleViewModel(googleViewModel: GoogleViewModel): ViewModel

}