/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Under Construction Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import { motion } from "framer-motion";
import { AlertTriangle } from "lucide-react";

export default function UnderConstruction() {
    return (
        <div className="flex flex-col items-center justify-center h-screen text-center p-6">
            <motion.div
                animate={{ rotate: [0, -5, 5, -5, 5, 0] }}
                transition={{ repeat: Infinity, duration: 2 }}
                className="mb-4"
            >
                <AlertTriangle className="w-16 h-16 text-yellow-500" />
            </motion.div>
            <h1 className="text-2xl font-bold text-gray-800">Page Under Construction</h1>
            <p className="text-gray-600 mt-2">
                We're working on this section. Check back soon!
            </p>
        </div>
    );
}
