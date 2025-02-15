import { styled } from 'styled-components'

export const Container = styled.div`
  border-radius: 0.25rem;
  overflow: hidden;
  position: relative;
  width: 100%;
  height: 0.5rem;
  background-color: ${({ theme }) => theme.colors.background};
`

interface BarProps {
  $progress: number
  $color?: string
}

export const Bar = styled.div<BarProps>`
  height: 100%;
  width: ${({ $progress }) => `${$progress}%`};
  background-color: ${({ $color, theme }) =>
    $color || theme.colors.secondaryForeground};
`
