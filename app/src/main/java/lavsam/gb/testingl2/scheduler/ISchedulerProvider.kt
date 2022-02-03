package lavsam.gb.testingl2.scheduler

import io.reactivex.Scheduler

interface ISchedulerProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}