package frc.robot.subsystems
import com.revrobotics.spark.SparkBase
import com.revrobotics.spark.SparkLowLevel
import com.revrobotics.spark.SparkMax
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode
import com.revrobotics.spark.config.SparkMaxConfig
import edu.wpi.first.math.MathUtil
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Current
import frc.robot.utils.RotationalDirection
import edu.wpi.first.units.Units.Amps
import edu.wpi.first.units.Units.Volts
import edu.wpi.first.units.measure.Voltage


class Shooter (private val config: ShooterConfiguration) {
    private val motorController = SparkMax(config.motorControllerId, SparkLowLevel.MotorType.kBrushless)
    private val motorFollower = SparkMax(config.motorFollower, SparkLowLevel.MotorType.kBrushless)


    fun setVoltage(voltage: Voltage) {
        val clampVoltage = MathUtil.clamp(voltage.`in`(Volts), Volts.of(-12.0).`in`(Volts), Volts.of(12.0).`in`(Volts))
        motorController.setVoltage(clampVoltage)
    }

    fun stopMotor() {
        motorController.setVoltage(0.0)
    }

    init {
        configureMotorInterface()
    }
    private fun configureMotorInterface () {
        val SparkConfig = SparkMaxConfig()
        val followerConfig = SparkMaxConfig()

        with(SparkConfig) {
            idleMode(config.neutralMode).inverted(
                config.motorDirection.opposite() == RotationalDirection. CounterClockwise
            ).smartCurrentLimit(config.motorCurrentLimit.`in`(Amps).toInt())
        }
        followerConfig.apply (SparkConfig).follow(config.motorControllerId, false)

        motorController.clearFaults()
        motorFollower.clearFaults()

        motorController.configure(SparkConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters)
        motorFollower.configure(followerConfig, SparkBase.ResetMode.kResetSafeParameters, SparkBase.PersistMode.kPersistParameters)
    }

}