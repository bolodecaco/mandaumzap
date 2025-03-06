import { Button } from '@/components/button'
import { Main } from '@/lib/styled/global'
import styled from 'styled-components'

export const StyledMain = styled(Main)`
  flex-direction: row;
`
export const Delete = styled(Button)`
  color: ${({ theme }) => theme.colors.tertiary};
`

export const List = styled.div`
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  height: 100%;
  width: 100%;
  max-height: 100%;
  overflow: hidden;
  overflow-y: auto;
  margin-left: -0.75rem;
`

export const DivInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 0.5rem;
`
export const Text = styled.span`
  font-weight: bold;
  font-size: 0.875rem;
  color: ${({ theme }) => theme.colors.title};
`
export const Section = styled.div`
  margin-bottom: 1rem;
`

export const SectionHeader = styled.div`
  display: flex;
  align-items: center;
  cursor: pointer;
  font-size: 1rem;
  font-weight: normal;
  color: ${({ theme }) => theme.colors.title};
  margin-bottom: 0.5rem;
  gap: 0.25rem;
  display: flex;
  align-items: center;
`

export const CardGrid = styled.div<{ $expanded: boolean }>`
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  opacity: ${({ $expanded }) => ($expanded ? '1' : '0')};
  margin-top: ${({ $expanded }) => ($expanded ? '0' : '-1rem')};
  height: ${({ $expanded }) => ($expanded ? 'auto' : '0')};
  pointer-events: ${({ $expanded }) => ($expanded ? 'all' : 'none')};
  transition: all 0.2s ease-in-out;
`
