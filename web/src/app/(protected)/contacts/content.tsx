'use client'

import { Row } from '@/lib/styled/global'
import { Chats } from './sections/chats'
import { Lists } from './sections/lists'

export const Content = () => {
  return (
    <Row style={{ flex: 1, minHeight: 0 }}>
      <Chats />
      <Lists />
    </Row>
  )
}
