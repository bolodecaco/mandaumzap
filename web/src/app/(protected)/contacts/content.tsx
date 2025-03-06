'use client'

import { Row } from '@/lib/styled/global'
import dynamic from 'next/dynamic'
import Lists from './sections/lists'
import { LoaderContainer, Spinner } from './sections/styles'

const Chats = dynamic(() => import('./sections/chats'), {
  ssr: false,
  loading: () => (
    <LoaderContainer style={{ flex: 3 }}>
      <Spinner />
    </LoaderContainer>
  ),
})

export const Content = () => {
  return (
    <Row style={{ flex: 1, minHeight: 0 }}>
      <Chats />
      <Lists />
    </Row>
  )
}
