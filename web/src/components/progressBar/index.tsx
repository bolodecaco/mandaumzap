import { Bar, Container } from './styles'

interface ProgressBarProps {
  progress: number
  color?: string
}

export const ProgressBar = ({ progress = 0, color }: ProgressBarProps) => {
  return (
    <Container>
      <Bar $color={color} $progress={progress} />
    </Container>
  )
}
