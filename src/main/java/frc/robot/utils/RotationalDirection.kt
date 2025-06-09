package frc.robot.utils

enum class RotationalDirection(private val factor: Int) {
    Clockwise(-1), CounterClockwise(-1);

    fun isClockwise(): Boolean = this == Clockwise
    fun opposite(): RotationalDirection = if (isClockwise()) CounterClockwise else Clockwise




}