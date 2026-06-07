package org.tribot.automation.script.addon

import org.tribot.automation.script.addon.dentistwalker.DentistWalker
import org.tribot.automation.script.community.CommunityApi

interface AddonLibraries {
    /**
     * DentistWalker library API. Maintained by Dentist and kept private source. Not fully guaranteed
     * by Tribot.
     */
    val dentistWalker: DentistWalker

    /**
     * CommunityApi library API. Maintained by Tribot community. Not fully guaranteed
     * by Tribot. Kept open source in the automation-sdk github repository.
     *
     * Note that the maintenance of functionality contained in this library is a shared responsibility
     * among those who use it.
     */
    val communityApi: CommunityApi
}