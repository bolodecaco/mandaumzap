'use client'

import { Row } from '@/lib/styled/global'

import dynamic from 'next/dynamic'

const Chats = dynamic(() => import('./sections/chats'), { ssr: false })
const Lists = dynamic(() => import('./sections/lists'), { ssr: false })

export const Content = () => {
  return (
    <Row style={{ flex: 1, minHeight: 0 }}>
      <Chats />
      <Lists />
    </Row>
  )
}
