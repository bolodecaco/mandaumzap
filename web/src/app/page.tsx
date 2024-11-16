import { PlanBadge } from '@/components/planBadge'
import { Page } from '@/lib/styled/global'
import { Back } from './styles'
export default function Home() {
  return (
    <Page>
      <Back>
        <PlanBadge type="free" />
      </Back>
    </Page>
  )
}
