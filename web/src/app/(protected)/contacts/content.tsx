'use client'

import { Row } from '@/lib/styled/global'
import { ChatsSection } from './chatSection'
import { ListSection } from './listSection'

export const Content = () => {
  return (
    <Row style={{ flex: 1, minHeight: 0 }}>
      <ChatsSection />
      <ListSection />
    </Row>
  )
}
