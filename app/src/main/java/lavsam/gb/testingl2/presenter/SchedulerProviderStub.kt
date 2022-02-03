package lavsam.gb.testingl2.presenter

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import lavsam.gb.testingl2.scheduler.ISchedulerProvider

class SchedulerProviderStub: ISchedulerProvider {
    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()
}