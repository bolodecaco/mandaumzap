'use client'

import { Row } from '@/lib/styled/global'
import { useState } from 'react'
import { ChatsSection } from './chatSection'
import { ListSection } from './listSection'

export const Content = () => {
  const [openSelectorChats, setOpenSelectorChats] = useState<string | null>(
    null,
  )
  const [openSelectorLists, setOpenSelectorLists] = useState<string | null>(
    null,
  )

  const handleOpenSelectorChats = (name: string) => {
    setOpenSelectorChats((prev) => (prev === name ? null : name))
  }

  const handleOpenSelectorLists = (name: string) => {
    setOpenSelectorLists((prev) => (prev === name ? null : name))
  }

  return (
    <Row style={{ flex: 1, minHeight: 0 }}>
      <ChatsSection
        openSelector={openSelectorChats}
        handleOpenSelector={handleOpenSelectorChats}
      />
      <ListSection
        openSelector={openSelectorLists}
        handleOpenSelector={handleOpenSelectorLists}
      />
    </Row>
  )
}
