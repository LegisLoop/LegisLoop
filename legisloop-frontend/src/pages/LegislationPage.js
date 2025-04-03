/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationPage Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
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
        <div className="flex flex-col min-h-screen">
            <NavBar />
            <div className="flex-1 flex flex-col lg:flex-row items-stretch">
                <LegislationSideBar votes={votes} />
                <div className="flex-1 p-4 md:p-6 mx-auto w-full max-w-full lg:max-w-[900px]">
                    <LegislationVisualizer mimeType="application/pdf" mimeId={SamplePdf}/>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default LegislationPage;
