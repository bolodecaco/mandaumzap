'use client'

import { ElementType } from 'react'
import { Title, Wrapper } from './styles'

export interface PlanBadgeProps {
  title: string
  variant: 'free' | 'premium'
  icon?: ElementType
}

export const PlanBadge = ({ title, variant, icon: Icon }: PlanBadgeProps) => (
  <Wrapper variant={variant}>
    {Icon && <Icon size={16} />}
    <Title variant={variant}>{title}</Title>
  </Wrapper>
)
