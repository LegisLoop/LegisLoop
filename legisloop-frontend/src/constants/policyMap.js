import {
    HeartIcon,
    CurrencyDollarIcon,
    BookOpenIcon,
    GlobalAmericasIcon,
    HandRaisedIcon,
    SparklesIcon,
    ScaleIcon,
    ShieldIcon,
    IdentificationIcon,
    LockIcon,
    HomeIcon,
    BuildingBridgeIcon,
} from "../components/Icons/Icons";
const policyMap = [
    { key: "PUBLIC_HEALTH", label: "Public Health", icon: <HeartIcon /> },
    { key: "ECONOMIC_POLICY", label: "Economic Policy", icon: <CurrencyDollarIcon /> },
    { key: "EDUCATION_POLICY", label: "Education Policy", icon: <BookOpenIcon /> },
    { key: "ENVIRONMENTAL_POLICY", label: "Environmental Policy", icon: <GlobalAmericasIcon /> },
    { key: "RELIGIOUS_POLICY", label: "Religious Policy", icon: <HandRaisedIcon /> },
    { key: "LGBTQ_RIGHTS", label: "LGBTQ+ Rights", icon: <SparklesIcon /> },
    { key: "CIVIL_RIGHTS", label: "Civil Rights", icon: <ScaleIcon /> },
    { key: "PUBLIC_SAFETY", label: "Public Safety", icon: <ShieldIcon /> },
    { key: "IMMIGRATION", label: "Immigration", icon: <IdentificationIcon /> },
    { key: "DATA_PRIVACY", label: "Data Privacy", icon: <LockIcon /> },
    { key: "HOUSING_URBAN_POLICY", label: "Housing Urban Policy", icon: <HomeIcon /> },
    { key: "INFRASTRUCTURE", label: "Infrastructure", icon: <BuildingBridgeIcon /> },
];
export default policyMap;