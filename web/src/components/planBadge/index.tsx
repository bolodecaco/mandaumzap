'use client'

import { Title, Wrapper } from './styles'

const planTitles = {
  free: 'Plano Gratuito',
  premium: 'Plano Premium',
}

export interface PlanBadgeProps {
  type: keyof typeof planTitles
}

export const PlanBadge = ({ type }: PlanBadgeProps) => (
  <Wrapper type={type}>
    <Title type={type}>{planTitles[type]}</Title>
  </Wrapper>
)
