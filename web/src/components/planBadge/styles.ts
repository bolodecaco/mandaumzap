import { styled } from 'styled-components'
import { PlanBadgeProps } from '.'

export const Wrapper = styled.div<{ type: PlanBadgeProps['type'] }>`
  background-color: ${({ theme, type }) =>
    type === 'free'
      ? theme.variants.plan.free['background-color']
      : theme.variants.plan.premium['background-color']};
  border-radius: 1rem;
  border: 1px solid
    ${({ theme, type }) =>
      type === 'free'
        ? theme.variants.plan.free['border-color']
        : theme.variants.plan.premium['border-color']};
  padding: 0.25rem 0.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-direction: row;
  gap: 0.25rem;
`

export const Title = styled.h3<{ type: PlanBadgeProps['type'] }>`
  color: ${({ theme, type }) => {
    return type === 'free'
      ? theme.variants.plan.free.color
      : theme.variants.plan.premium.color
  }};
  font-size: 1rem;
  font-weight: 600;
`
