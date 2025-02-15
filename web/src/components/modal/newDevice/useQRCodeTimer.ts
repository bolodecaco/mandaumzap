import { useEffect, useState } from 'react'

interface UseQRCodeTimerProps {
  initialTime: number
  onExpire: () => void
  qrCode: string | null
}

export const useQRCodeTimer = ({
  initialTime,
  onExpire,
  qrCode,
}: UseQRCodeTimerProps) => {
  const [progress, setProgress] = useState(100)
  const [remainingTime, setRemainingTime] = useState(initialTime)

  const resetTimer = () => {
    setProgress(100)
    setRemainingTime(initialTime)
  }

  useEffect(() => {
    if (!qrCode) return

    const duration = initialTime * 1000
    const interval = 16
    const decrementValue = (interval / duration) * 100

    const timer = setInterval(() => {
      setProgress((prev) => {
        const newProgress = prev - decrementValue
        if (newProgress <= 0) {
          clearInterval(timer)
          onExpire()
          return 0
        }
        return newProgress
      })

      setRemainingTime((prev) => {
        const newTime = prev - interval / 1000
        return newTime <= 0 ? 0 : newTime
      })
    }, interval)

    return () => clearInterval(timer)
  }, [initialTime, onExpire, qrCode])

  return { progress, remainingTime, resetTimer }
}
