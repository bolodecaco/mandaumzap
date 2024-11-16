import { styled } from 'styled-components'
import { PlanBadgeProps } from '.'

export const Wrapper = styled.div<{ variant: PlanBadgeProps['variant'] }>`
  ${({ theme, variant }) => {
    const styles = theme.variants.plan[variant]
    return { ...styles }
  }}

  border-radius: 1rem;
  border: 1px solid;
  padding: 0.25rem 0.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  gap: 0.25rem;
`

export const Title = styled.p<{ variant: PlanBadgeProps['variant'] }>`
  ${({ theme, variant }) => {
    const styles = theme.variants.plan[variant]
    return styles.color
  }}

  font-size: 0.875rem;
  font-weight: 500;
`
