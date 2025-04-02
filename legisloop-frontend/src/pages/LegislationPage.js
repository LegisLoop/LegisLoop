import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationVisualizer from "../components/LegislationVisualizer/LegislationVisualizer";
import SamplePdf from "../components/LegislationVisualizer/sample.pdf";

function LegislationPage() {
    const votes = [
        { representative: "John Doe", decision: "Yea" },
        { representative: "Jane Smith", decision: "Nay" },
        { representative: "Alex Johnson", decision: "Abstain" }
    ];

    return (
        <>
            <NavBar />
            <div className="flex min-h-screen">
                <div className="w-[20rem] flex-shrink-0">
                    <LegislationSideBar votes={votes} />
                </div>
                <div className="flex-1 p-6 mx-auto max-w-[900px] lg:mx-2">
                    <LegislationVisualizer mimeType="application/pdf" mimeId={SamplePdf} /></div>
            </div>
            <Footer />
        </>
    );
}

export default LegislationPage;
